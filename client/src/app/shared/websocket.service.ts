import { Subject } from 'rxjs/Subject';
import { Observer } from 'rxjs/Observer';
import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';

@Injectable()
export class WebSocketService {

    private subject: Subject<MessageEvent>;

    public connect(url): Subject<MessageEvent> {
        if (!this.subject) {
            this.subject = this.create(url);
        }

        return this.subject;
    }

    private create(url): Subject<MessageEvent> {
        let ws = new WebSocket(url);

        let observable = Observable.create((obs: Observer<MessageEvent>) => {
            ws.onmessage = (msg) => obs.next(msg);
            ws.onerror = (msg) => obs.error(msg);
            ws.onclose = (close) => obs.complete();

            return () => ws.close();
        });

        let observer = {
            next: (data: Object) => {
                if (ws.readyState === WebSocket.OPEN) {
                    ws.send(JSON.stringify(data));
                }
            }
        };

        return Subject.create(observer, observable);
    }
}
