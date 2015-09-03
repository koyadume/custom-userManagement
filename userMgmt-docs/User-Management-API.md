# Context root
```
/userMgmt-service/v1.0
```

## Users

### Search users
```
Get /users?query=(attr1=value1&attr2=value2)
```

### Get user
```
Get /users/{id}
```

### Create user
```
POST /users
```

### Update user
```
PUT /users/{id}
```

### Delete users
```
DELETE /users?userIds=<Comma seperated list of user ids>
```

## Groups

### Search groups
```
Get /groups?query=(attr1=value1&attr2=value2)
```

### Get group
```
Get /groups/{id}
```

### Create group
```
POST /groups
```

### Update group
```
PUT /groups/{id}
```

### Delete groups
```
DELETE /groups?groupIds=<Comma seperated list of group ids>
```

