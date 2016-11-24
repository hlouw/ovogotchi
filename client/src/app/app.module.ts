import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { StoreModule } from '@ngrx/store';

import { emotionReducer } from './emotion';
import { AppComponent } from './app.component';
import { CharacterComponent } from './character/character.component';
import { AvatarComponent } from './character/avatar/avatar.component';
import { StatusComponent } from './character/status/status.component';
import { ThoughtComponent } from './character/thought/thought.component';
import { CharacterStateService } from './character/character-state.service';
import { WebSocketService } from './shared/websocket.service';

@NgModule({
  declarations: [
    AppComponent,
    CharacterComponent,
    AvatarComponent,
    StatusComponent,
    ThoughtComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    StoreModule.provideStore({ emotion: emotionReducer })
  ],
  providers: [
    CharacterStateService,
    WebSocketService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
