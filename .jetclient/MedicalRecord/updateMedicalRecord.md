```toml
name = 'updateMedicalRecord'
method = 'PUT'
url = 'http://localhost:8080/medicalRecord'
sortWeight = 3000000
id = '71cc8fc6-2024-4f90-96ad-29e510d317dc'

[body]
type = 'JSON'
raw = '{ "firstName":"John", "lastName":"Boyd", "birthdate":"03/06/1984", "medications":["tylenol:1000mg", "asprin:500mg"], "allergies":["nillacilan"] }'
```