# Mercari Test - Solution by Victor Negrão

## Selected Option: B (Base App) ##
## Notes: ##

- Even though the Timeline should be a grid with two columns, when in the landscape orientation I chose to use a 3-columns grid.

- I added a menu with some sorting options for the products.

- There are some options that can be modified in the app:
	1. In the class MyApplication.java, there's a variable called 'fixedCategories' - it determines whether the app should use the fixed and ordered categories (Men, All, Women) or dynamic ones - in case there are other categories for testing.
	2. In the class Controller.java, there's a variable called 'useCache' - it determines whether the app should cache HTTP responses and therefore offer offline visualization of the products as long as they have been accessed before.

- The app automatically detects changes in connectivity, so if the "useCache" option is disabled and the user enters the application while disconnected, the API calls will be automatically made when he turns on internet by any means. Despite that, there are Empty State screens and a SwipeRefreshLayout function in the app.


### Contact ###
Victor Luiz Santos Negrão - victorlsn@gmail.com