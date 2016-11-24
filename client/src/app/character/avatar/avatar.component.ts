import { Component, Input } from '@angular/core';
import { EmotionalState } from '../../emotion';

@Component({
  selector: 'app-avatar',
  templateUrl: './avatar.component.html'
})
export class AvatarComponent {
  @Input() state: EmotionalState;

  getStateUrl() {
    switch (this.state) {
      case EmotionalState.HAPPY:
        return 'assets/happy.gif';
      case EmotionalState.NEUTRAL:
        return 'assets/neutral.gif';
      case EmotionalState.SAD:
        return 'assets/sad.gif';
    }
  }
}
