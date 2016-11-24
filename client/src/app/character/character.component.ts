import { Component, OnInit } from '@angular/core';
import { EmotionalState } from '../emotion';

@Component({
  selector: 'app-character',
  templateUrl: './character.component.html',
})
export class CharacterComponent {

  constructor() { }

  state: EmotionalState = EmotionalState.NEUTRAL;
  wellbeing: number = 60;
  thought: string = 'https://avatars1.githubusercontent.com/u/106760?v=3&s=400';
}
