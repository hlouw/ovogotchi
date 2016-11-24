import { Component, OnInit } from '@angular/core';
import { EmotionalState } from '../emotion';

@Component({
  selector: 'app-character',
  templateUrl: './character.component.html',
})
export class CharacterComponent {

  constructor() { }

  state: EmotionalState = EmotionalState.SAD;
  wellbeing: number = 50;
}
