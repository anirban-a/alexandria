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
