import { HttpInterceptorFn } from "@angular/common/http";

export const RequestInterceptor : HttpInterceptorFn = (oldReq,next)=>{
    //C'est un middleware qui qjoute un header a toutes les requetes http
   
    //On recupere le token en session     
    const authToken = localStorage.getItem('token') || '';

    const newReq = oldReq.clone({
        setHeaders: {
            Authorization: `Bearer ${authToken}`
        }
    });
    // console.log('Headers envoye: ' + JSON.stringify(newReq.headers));
    

    return next(newReq);

}