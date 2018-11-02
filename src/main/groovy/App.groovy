 

import groovy.sql.Sql
class App {

	
	def printDbValues(sql) {
		  sql.eachRow("select * from users") { printrow ->
		  println "$printrow.id $printrow.name $printrow.email"
		  }
	}
	
	def run(){
		
		
		def sql = Sql.newInstance("jdbc:mysql://localhost:3307/test", "root","12345678", "com.mysql.jdbc.Driver")
		 
		//Drop table if it already exists
		sql.execute('drop TABLE users')
		 
		//Create table users
		sql.execute('create table users(id INT NOT NULL AUTO_INCREMENT,name VARCHAR(15) NOT NULL,email VARCHAR(15), PRIMARY KEY(id))')
		 
		//Insert some values
		sql.execute('insert into users values(null,"Lucas Ko","a@test.com")')
		sql.execute('insert into users values(null,"Tim Parker","b@test.com")')
		sql.execute('insert into users values(null,"Kevin Smith","c@test.com")')
		 
		//We can also insert by prepared statements by
		sql.execute('insert into users values(null,?,?)',['Peter Parker','d@test.com'])
		 
		//Or for better reuse
		def InsertQuery = "insert into users values(null,?,?)"
		sql.execute(InsertQuery,['Harry Porter','e@test.com'])
		 
		//Print single row
		println "\n------------------------------Print Single Row--------------------------------------------------------\n"
		def row = sql.firstRow('select * from users where name = "Kevin Smith"')
		println "Row: id = ${row.id} and email = ${row.email}"
		 
		//Printing Multiple rows with rows (user handles each row with each)
		println "\n------------------------------Print Multiple Rows (using rows)----------------------------------------\n"
		println "ID    NAME         EMAIL"
		def fetch = sql.rows("select * from users")
		fetch.each { it ->
		 println it.id + " " + it.name + " " +it.email
		}
		 
		//Printing Multiple rows with eachRow (closure passed in as the second parameter should handle each row)
		println "\n------------------------------Print Multiple Rows (using eachRow)-------------------------------------\n"
		sql.eachRow("select * from users") { printrow ->
		 println "$printrow.id $printrow.name $printrow.email"
		}
		 
		//Delete a row
		println "\n------------------------------Delete an entry from table----------------------------------------------\n"
		sql.execute('delete from users where id = ?' , [5])
		printDbValues(sql)
		 
		//Update a value
		println "\n------------------------------Update an entry in table------------------------------------------------\n"
		sql.executeUpdate('update users set email = ? where id=4', ["f@test.com"])
		printDbValues(sql)
		 
		//Define a function for print reuse
		println "\n------------------------------------------------------------------------------------------------------\n"
	 
		
		
	}
	
	static main(args) {
	 
		def app = new App()
		app.run()
		
	}
	
	

}
