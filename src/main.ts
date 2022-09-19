import express, {Request,Response,Application, query} from 'express';


class ApplicationMain{
    private app;
    private port;

    constructor(port:number){
        this.app = express();  
        this.port = port;
    }

    run():void{
        
        this.app.get("/:name", (req, res)=>{
            res.send(`Hello ${req.params.name}`)
        })
        
        this.app.listen( this.port, ()=>{
            console.log(`Application started on at http://localhost:${this.port}`)
        })
    }
}

const application = new ApplicationMain(8080);
application.run();