import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {NgbConfig} from '@ng-bootstrap/ng-bootstrap/config'
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ReactiveFormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('PersonalFinanceAngular');
  constructor(NgbConfig: NgbConfig) {
    NgbConfig.animation = false;
  }
}
