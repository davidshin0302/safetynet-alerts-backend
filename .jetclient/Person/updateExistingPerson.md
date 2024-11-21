```toml
name = 'updateExistingPerson'
method = 'PUT'
url = 'http://localhost:8080/person'
sortWeight = 3000000
id = '988d08d5-89a1-42b5-85ce-7c90d0c4b958'

[body]
type = 'JSON'
raw = '''
{
  "firstName": "John",
  "lastName": "Doe",
  "address": "921 Ocean ave",
  "city": "sunnyside",
  "zip": "11104",
  "phone": "123-456-8970",
  "email": "jdoe@email.com"
}'''
```
