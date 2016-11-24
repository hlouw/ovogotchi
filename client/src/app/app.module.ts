import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { CharacterComponent } from './character/character.component';
import { AvatarComponent } from './character/avatar/avatar.component';
import { StatusComponent } from './character/status/status.component';

@NgModule({
  declarations: [
    AppComponent,
    CharacterComponent,
    AvatarComponent,
    StatusComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
