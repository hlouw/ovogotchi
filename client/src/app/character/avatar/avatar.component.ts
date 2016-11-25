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
      case EmotionalState.ILL:
        return 'assets/ill.gif';
      case EmotionalState.ANGRY:
        return 'assets/angry.png';
      case EmotionalState.LONELY:
        return 'assets/lonely.png';
    }
  }
}
