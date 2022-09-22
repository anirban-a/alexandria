import { Container, Service, Inject } from 'typedi';
import { Request, Response } from 'express'
import createLogger from 'logging';
import 'reflect-metadata';
import {UserRepository} from '../repository/user'

@Service()
export class UserController {
    private userRepository: UserRepository;
    private logger = createLogger('UserController');

    constructor(userRepository: UserRepository) {
        this.userRepository = userRepository;
    }

    public getUserByID = async (req: Request<{ id: number }>, response: Response) => {
            this.logger.info(`Received request !!`)
            const result = await this.userRepository.getUserById(req.params.id);
            response.send(result.data)
        };
}