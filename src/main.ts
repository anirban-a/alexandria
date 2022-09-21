import express, {Request,Response,Application, query, response} from 'express';
import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';

dotenv.config()

const CONNECTION_URL:string = process.env.SUPABASE_URL || "";
const CONNECTION_KEY:string = process.env.SUPABASE_ANON_KEY || "";
const SERVER_PORT:number = parseInt(process.env.PORT || "8080");

console.log(`Using database URL: ${CONNECTION_URL}`)
console.log(`Using database KEY ${CONNECTION_KEY}`)

const supabase = createClient(CONNECTION_URL, CONNECTION_KEY)

class ApplicationMain{
    private app;
    private port;

    constructor(port:number){
        this.app = express();  
        this.port = port;
    }

    private async setupDB():Promise<void>{
        const { data, error } = await supabase.auth.signInWithOAuth({
            'provider': 'github'
        })
        
    }

    validateID() {
        return (req:Request<{ id: number}>, res:Response<any>, next:Function)=>{
            if(isNaN(req.params.id))
                return res.status(400).send('Invalid ID value');
            next();
        }
    }

    run():void{
        this.setupDB()

        this.app.get("/:id", this.validateID(), async (req: Request<{ id: number}>, response)=>{

            // select username from Test where id=:id
            const result = await supabase.from('User')
            .select('username')
            .eq('id', req.params.id)
            .single()

            
            response.send(result.data)
        })
        
        this.app.listen( this.port, ()=>{
            console.log(`Application started on at http://localhost:${this.port}`)
        })
    }
}

const application = new ApplicationMain(SERVER_PORT);
application.run();