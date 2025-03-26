import { AbstractControl, AsyncValidatorFn, ValidationErrors } from '@angular/forms';
import { Observable, map, catchError, of } from 'rxjs';
import { CepService } from '../services/cep-service/cep.service';

export function cepValidator(cepService: CepService): AsyncValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    const cep = control.value;
    
    if (!cep) {
      return of(null);
    }

    const cepLimpo = cep.replace(/\D/g, '');

    if (cepLimpo.length !== 8) {
      return of({ cepInvalido: true });
    }

    return cepService.consultarCep(cepLimpo).pipe(
      map(response => {
        const isValid = Boolean(
          response && 
          response.cep && 
          response.logradouro && 
          response.bairro && 
          response.localidade && 
          response.uf
        );
        return isValid ? null : { cepInvalido: true };
      }),
      catchError(() => of({ cepInvalido: true }))
    );
  };
}