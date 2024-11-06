```toml
name = 'addNewPerson'
method = 'POST'
url = 'http://localhost:8080/person'
sortWeight = 2000000
id = '899012eb-d801-45c0-b527-b624e97d19fc'

[body]
type = 'JSON'
raw = '''
{
  "firstName": "John",
  "lastName": "Doe",
  "address": "123 Main St",
  "city": "flushing",
  "zip": "11001",
  "phone": "123-456-8970",
  "email": "jdoe@email.com"
}'''
```
