import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-thought',
  templateUrl: './thought.component.html'
})
export class ThoughtComponent {
   @Input() thought: string;
}
