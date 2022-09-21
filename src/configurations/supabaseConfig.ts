import { createClient } from '@supabase/supabase-js';
import { Service } from 'typedi';
import createLogger from 'logging';
import 'reflect-metadata';
import dotenv from 'dotenv';

dotenv.config()

const CONNECTION_URL: string = process.env.SUPABASE_URL || "";
const CONNECTION_KEY: string = process.env.SUPABASE_ANON_KEY || "";

console.log(`Using database URL: ${CONNECTION_URL}`)
console.log(`Using database KEY ${CONNECTION_KEY}`)

const supabase = createClient(CONNECTION_URL, CONNECTION_KEY)

@Service()
export class SupabaseConfig{
    private logger = createLogger('SupabaseConfig');

    constructor(){
        this.logger.info("Initiating DB setup..")
        this.setupDB();
    }

    private async setupDB(): Promise<void> {
        const { data, error } = await supabase.auth.signInWithOAuth({
            'provider': 'github'
        })

        if(error){
            this.logger.error(error.message);
        }

    }

    public getDBConnection(): any{
        return supabase;
    }
}