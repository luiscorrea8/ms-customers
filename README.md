### curso
>- https://udemy.com/course/mongodb-with-spring-boot-spring-data-mongorepository-mlab-cloudfoundry/

### instalar mongodb
>- https://www.youtube.com/watch?v=2cWZ0lFbJoY&t=381s
>- https://www.youtube.com/watch?v=hQMaS0GTR-8

### aplicación roboto
>- https://github.com/Studio3T/robomongo

### comandos 
>- mongod --storageEngine=mmapv1

### descargar shell
>- https://www.mongodb.com/try/download/shell

### usar terminal
>- mongod.exe solo una vez
>- mongosh.exe
>- use spring
>- db.getCollection('student').find({})
[
  {
    _id: ObjectId("64f9f69290bf5e09cc154cf0"),
    name: 'Jhon',
    mail: 'john@gmail.com'
  },
  {
    _id: ObjectId("64f9f7e590bf5e09cc154d17"),
    name: 'Peter',
    mail: 'peter@gmail.com',
    department: { department_name: 'Computer Science', location: 'INDIA' }
  },
  {
    _id: ObjectId("64f9fd0e90bf5e09cc154db6"),
    name: 'Steve',
    mail: 'steve@gmail.com',
    subjects: [
      { subject_name: 'Java', marks_obtained: 70 },
      { subject_name: 'MongoDB', marks_obtained: 80 }
    ]
  }
]