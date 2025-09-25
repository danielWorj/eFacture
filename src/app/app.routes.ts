import { Routes } from '@angular/router';
import { Facture } from './Components/facture/facture';

export const routes: Routes = [ 

   {
     path : '',
     loadComponent : ()=>import('./Components/dashboard/dashboard').then((d)=>d.Dashboard),
   },
    {
     path : 'dashboard',
     loadComponent : ()=>import('./Components/dashboard/dashboard').then((d)=>d.Dashboard),
   }, 
    {
     path : 'facture',
     loadComponent : ()=>import('./Components/facture/facture').then((f)=>f.Facture),
    
   }, 
    {
     path : 'facturation',
     loadComponent : ()=>import('./Components/facturation/facturation').then((f)=>f.Facturation),
    
   }, 
    {
     path : 'client',
     loadComponent : ()=>import('./Components/clients/clients').then((c)=>c.Clients),
   }, 
    {
     path : 'paiements',
     loadComponent : ()=>import('./Components/paiement/paiement').then((p)=>p.Paiement),
   }, 
//   {
//     path : 'dashboard',
//     loadComponent : ()=>import('./core/logic/dashboard/dashboard.component').then((d)=>d.DashboardComponent),
//   },
//   {
//     path : 'admin',
//     loadComponent : ()=>import('./core/logic/dashboard/dashboard.component').then((d)=>d.DashboardComponent),
//   },
];
