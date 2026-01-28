import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreatePaste } from './components/create-paste/create-paste';
import { ViewPaste } from './components/view-paste/view-paste';
import { LayoutComponent } from './components/layout/layout';

const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', redirectTo: 'create-paste', pathMatch: 'full' },
      { path: 'create-paste', component: CreatePaste },
      { path: 'view-paste', component: ViewPaste }
    ]
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
