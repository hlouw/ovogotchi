import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

import { WebSocketService } from '../shared/websocket.service';

export interface CharacterStateMessage {
  state: string;
  wellbeing: number;
}

@Injectable()
export class CharacterStateService {

  private messages: Subject<CharacterStateMessage>;
  private url = 'ws://localhost:8080/ws';

  constructor(ws: WebSocketService) {
    this.messages = <Subject<CharacterStateMessage>>ws
      .connect(this.url)
      .map((response: MessageEvent): CharacterStateMessage => {
        let data = JSON.parse(response.data);
        return {
          state: data.state,
          wellbeing: data.wellbeing
        };
      });
  }

  stateStream(): Observable<CharacterStateMessage> {
    return this.messages.asObservable();
  }

}
