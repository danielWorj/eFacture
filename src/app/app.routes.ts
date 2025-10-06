import { Routes } from '@angular/router';
import { Facture } from './Components/facture/facture';
import { Facturation } from './Components/facturation/facturation';

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
     component : Facturation,
    
   }, 
    {
     path : 'client',
     loadComponent : ()=>import('./Components/clients/clients').then((c)=>c.Clients),
   }, 
    {
     path : 'paiements',
     loadComponent : ()=>import('./Components/paiement/paiement').then((p)=>p.Paiement),
   }, 
   {
     path : 'configuration',
     loadComponent : ()=>import('./Components/configuration/configuration').then((c)=>c.Configuration),
   },
//   {
//     path : 'admin',
//     loadComponent : ()=>import('./core/logic/dashboard/dashboard.component').then((d)=>d.DashboardComponent),
//   },
];
