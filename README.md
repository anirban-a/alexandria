# Alexandria

An application to help people exchange books among themselves for free.

## About the project
Pontential Book Sharing Rules:
1. Users must have one offered book to request a book.
2. The exchange should be N:N. A user can ask for only N books if can give N books.


## Setting up the project
1. Install [`Node.js`](https://nodejs.org/en/download/). Choose the LTS version.
2. Setup `Typescript`:

	a. To install the Typescript compiler package, run the following command:

		npm install -g typescript
	b. Run `tsc --version` to confirm if the compiler is installed.
3. Using Typescript with `Node.js`: Run the following
	
		npm install -D typescript

4. Install `ts-node` to run the service with lesser commands:
	
		npm install -D ts-node
	`Ts-node` allows us to point to a Typescript file. It will run .ts, compile it and run it with Node.js for us.

5. Install `Express.js`:

		npm install express

6. **Note:** We need to make sure we have `Node.js` types checking package installed whenever writing Typescript using `Node.js`.

		npm install -D @types/node

7. We also need to use [Express type definations](https://www.npmjs.com/package/@types/express):

		npm install -D @types/express

8. To start the application, `cd` into the directory under `/alexandria` and run:

		npm run start

	The application should start on `http://localhost:8080`. Hit `http://localhost:8080/<your_name>` for a greeting!!:boom: 

9. Install `dotenv` to read configurations from an environment file:

		npm install dotenv
	
10. Install the dependency for [**Supabase**](https://supabase.com/) (an amazing realtime and managed PostgreSQL service):

		npm install @supabase/supabase-js@rc

	Also, create an account using your Github (because we will use `OAuth` to establish connection with the database. Once the account has been created, collect the `annon_key` and project `URL`.

11. Create a `.env` file at the root level of the project to keep the secret credentials and put your Supabase `URL` and `annon_key` collected in previous step and place it in the `.env` file:

		SUPABASE_URL="<your_project_url>"
	
		SUPABASE_ANON_KEY="<your_annon_key>"

12. Dependency for logger:
	
		npm i logging

13. Install `typedi` for dependency injection:

		npm install typedi reflect-metadata
