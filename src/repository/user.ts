import { Service } from 'typedi';
import { SupabaseConfig } from '../configurations/supabaseConfig';
import 'reflect-metadata';

@Service()
export class UserRepository{
    private supabaseConfig:SupabaseConfig;

    constructor(supabaseConfig:SupabaseConfig){
        this.supabaseConfig = supabaseConfig;
    }

    public async getUserById(id:number):Promise<any>{
            // select username from Test where id=:id
            const result = await this.supabaseConfig.getDBConnection()
                .from('User')
                .select('username')
                .eq('id', id)
                .single()

            return result;
            
    }
}