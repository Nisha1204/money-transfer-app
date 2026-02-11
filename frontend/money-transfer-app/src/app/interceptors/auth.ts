import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // IMPORTANT: These must match a user in your database
  // In a real app, you would get these from a Login Form/Service
  const username = 'user1'; 
  const password = 'pwd';
  
  // Encode credentials for Basic Auth
  const basicAuthHeader = 'Basic ' + btoa(`${username}:${password}`);
  console.log('Sending Auth Header:', basicAuthHeader); // Debug line

  const authReq = req.clone({
    setHeaders: {
      Authorization: basicAuthHeader
    }
  });

  return next(authReq);
};