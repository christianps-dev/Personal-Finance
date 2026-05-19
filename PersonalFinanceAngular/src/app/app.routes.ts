import { Routes } from '@angular/router';
import { LoginPage } from './components/login-page/login-page';
import { SignupPage } from './components/signup-page/signup-page';
import { DashboardPage } from './components/dashboard-page/dashboard-page';
import { routeGuard } from './security/auth-security/route-guard-guard';
import { FinanceForm } from './components/finance-form/finance-form';
import { ProfilePage } from './components/profile-page/profile-page';
import { HistoryPage } from './components/history-page/history-component';

export const routes: Routes = [
    {
        path: 'login',
        component: LoginPage
    },
    {
        path: 'signup',
        component: SignupPage
    },
    {
      path: 'dashboard',
      component: DashboardPage,
      canActivate:[routeGuard]
    },
    {
      path: 'finances',
      component: FinanceForm,
      canActivate:[routeGuard]
    },
    {
      path: 'profile',
      component: ProfilePage,
      canActivate:[routeGuard]
    },
    {
      path: 'history',
      component: HistoryPage,
      canActivate:[routeGuard]
    },
    {
      path: '**',
      redirectTo: 'login'
    }

];
