import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { RegisterUser } from '../../services/auth-user/register-user';
import { UserRegisterDTO} from '../../models/auth-dto/user-register';

@Component({
  selector: 'app-signup-page',
  imports: [ReactiveFormsModule],
  templateUrl: './signup-page.html',
  styleUrl: './signup-page.css',
})
export class SignupPage {

  constructor(private registerUser : RegisterUser) {}

  registerForm = new FormGroup({
    username: new FormControl('',Validators.required),
    email: new FormControl('', [Validators.email, Validators.required]),
    password: new FormControl('', [Validators.minLength(8), Validators.required]),
  });

  registerUserSignUp(){
    const userDTO = this.registerForm.value as UserRegisterDTO;
    console.log(userDTO)
    this.registerForm.reset({
      username: "",
      email: "",
      password: ""
    })
    return this.registerUser.registerNewUser(userDTO);
  }

}
