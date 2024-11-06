```toml
name = 'deleteExistingPerson'
method = 'DELETE'
url = 'http://localhost:8080/person'
sortWeight = 4000000
id = '7cdfac4c-af0b-4724-9c8b-2671a175dc64'

[body]
type = 'JSON'
raw = '''
{
  "firstName": "John",
  "lastName": "Doe"
}'''
```
