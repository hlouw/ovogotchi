import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

import { WebSocketService } from '../shared/websocket.service';

export interface CharacterStateMessage {
  state: string;
  wellbeing: number;
  thought?: string;
}

@Injectable()
export class CharacterStateService {

  private messages: Subject<CharacterStateMessage>;

  constructor(ws: WebSocketService) {
    let url = 'ws://' + window.location.hostname + ':8080/ws';
    this.messages = <Subject<CharacterStateMessage>>ws
      .connect(url)
      .map((response: MessageEvent): CharacterStateMessage => {
        let data = JSON.parse(response.data);
        console.log(data);
        return {
          state: data.state,
          wellbeing: data.wellbeing,
          thought: data.thought
        };
      });
  }

  stateStream(): Observable<CharacterStateMessage> {
    return this.messages.asObservable();
  }

}
