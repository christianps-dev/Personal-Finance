import { inject, Injectable } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const routeGuard: CanActivateFn = (route, state) => {

  const localToken = sessionStorage.getItem('token');
  const localEmail = sessionStorage.getItem('email');
  const router = new Router;

  if(localEmail == null || localToken == null){
   router.navigate(["/login"])
    return false;
  }
  return true;
};
