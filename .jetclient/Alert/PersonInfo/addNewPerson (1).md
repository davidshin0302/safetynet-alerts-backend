```toml
name = 'addNewPerson (1)'
method = 'POST'
url = 'http://localhost:8080/person'
sortWeight = 2000000
id = '9b395241-3125-463d-81ab-d32e55687a0a'

[body]
type = 'JSON'
raw = '''
{
  "firstName": "John",
  "lastName": "Boyd",
  "address": "123 Main St",
  "city": "flushing",
  "zip": "11001",
  "phone": "123-456-8970",
  "email": "jdoe@email.com"
}'''
```
