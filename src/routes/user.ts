import { UserController } from "../controllers/user";
import express, { Request, Response } from 'express'
import { Container } from 'typedi';
import createLogger from 'logging';

const logger = createLogger('UserRoutes');

const userController:UserController = Container.get(UserController)


const app = express();
let userRoutes = express.Router();

logger.info(`Dependency check for ApplicationMain: ${app}`)

function validateID() {
    return (req: Request<{ id: number }>, res: Response<any>, next: Function) => {
        if (isNaN(req.params.id))
            return res.status(400).send('Invalid ID value');
        next();
    }
}
var userInfo = userController.getUserByID;
userRoutes.get("/:id", validateID(), userInfo)

// module.exports.userRoutes = route;
export default userRoutes;