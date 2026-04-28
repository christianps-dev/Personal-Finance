import { Component } from '@angular/core';
import { FormControl, FormControlName, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ChangePasswordDTO } from '../../../models/auth-dto/change-password-dto';
import { AsideComponent } from "../../aside-component/aside-component/aside-component";
import { AuthUser } from '../../../services/auth-user/auth-user';

@Component({
  selector: 'app-profile-page',
  imports: [ReactiveFormsModule, AsideComponent],
  templateUrl: './profile-page.html',
  styleUrl: './profile-page.css',
})
export class ProfilePage {

  changePasswordForm = new FormGroup({
    email : new FormControl(sessionStorage.getItem('email')),
    password : new FormControl("", [Validators.required, Validators.nullValidator, Validators.minLength(8)]),
    password2 : new FormControl("", [Validators.required, Validators.nullValidator, Validators.minLength(8)]),
    newPassword : new FormControl("", [Validators.required, Validators.nullValidator, Validators.minLength(8)])
  });

  constructor(private authUser: AuthUser){}


  public username = sessionStorage.getItem('username')
  public email = sessionStorage.getItem('email')

  public changeUserPassword(){
    const newPassword = this.changePasswordForm.value as ChangePasswordDTO;
    this.authUser.changePassword(newPassword);

    this.changePasswordForm.reset();
  }
}
