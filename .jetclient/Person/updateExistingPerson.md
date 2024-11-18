```toml
name = 'updateExistingPerson'
method = 'PUT'
url = 'http://localhost:8080/person'
sortWeight = 7000000
id = '988d08d5-89a1-42b5-85ce-7c90d0c4b958'

[body]
type = 'JSON'
raw = '''
{ "firstName":"John", "lastName":"Boyd", "address":"123 Main St", "city":"Oakland", "zip":"11101", "phone":"123-456-7890", "email":"jaboyd@email.com" }
'''
```
