import '@ngrx/core/add/operator/select';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs/Observable';

import { CharacterStateService } from './character-state.service';
import { OverallState, EmotionalState, STATE_UPDATE } from '../emotion';

@Component({
  selector: 'app-character',
  templateUrl: './character.component.html',
})
export class CharacterComponent implements OnInit, OnDestroy {

  private connection;

  state: Observable<EmotionalState>;
  wellbeing: Observable<number>;

  private emotionStore: Observable<OverallState>;

  constructor(
    private store: Store<any>,
    private stateService: CharacterStateService
  ) {
    this.emotionStore = this.store.select('emotion');
    this.state = this.emotionStore.select(s => s.state);
    this.wellbeing = this.emotionStore.select(s => s.wellbeing);
  }

  ngOnInit() {
    this.connection = this.stateService.stateStream().subscribe(message => {
      this.store.dispatch({ type: STATE_UPDATE,  payload: {
        state: message.state,
        wellbeing: message.wellbeing
      }});
    });
  }

  ngOnDestroy() {
    this.connection.unsubscribe();
  }
}
