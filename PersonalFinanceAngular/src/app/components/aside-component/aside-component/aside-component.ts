import { Component } from '@angular/core';
import { RouterLinkActive, RouterLinkWithHref } from '@angular/router';
import { GeneralServices } from '../../../services/general-service/general-services';

@Component({
  selector: 'app-aside-component',
  imports: [RouterLinkActive, RouterLinkWithHref],
  templateUrl: './aside-component.html',
  styleUrl: './aside-component.css',
})
export class AsideComponent{

  constructor(private generalS: GeneralServices) {}

  public logoutUser(){
    return this.generalS.logoutUser();
  }
}
