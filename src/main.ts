import express, { Request, Response, Application, query, response } from 'express';

import dotenv from 'dotenv';
import userRoutes from './routes/user'


dotenv.config()


const SERVER_PORT: number = parseInt(process.env.PORT || "8080");


class ApplicationMain {
    private app;
    private port;


    constructor(port: number) {
        this.app = express();
        this.port = port;
    }

    

    run(): void {
        
        this.app.use('/api', userRoutes)
        this.app.listen(this.port, () => {
            console.log(`Application started on at http://localhost:${this.port}`)
        })
    }
}

const application = new ApplicationMain(SERVER_PORT);
application.run();