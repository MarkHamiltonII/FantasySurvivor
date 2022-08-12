import { useContext, useState } from 'react';
import { useHistory } from 'react-router-dom';
import AuthContext from '../AuthContext';
import { loginFields } from "../constants/formFields";
import Errors from './Errors';
import FormAction from './FormAction';
import FormExtra from './FormExtra';
import Header from "./Header";
import Input from './Input';

const fields = loginFields;
let fieldsState = {};
fields.forEach(field => fieldsState[field.id] = '');

function Login() {
    const [loginState, setLoginState] = useState(fieldsState);
    const [errors, setErrors] = useState([]);

    const auth = useContext(AuthContext);
    const history = useHistory();

    const handleChange = (e) => {
        setLoginState({ ...loginState, [e.target.id]: e.target.value })
    }

    const handleSubmit=(e)=>{
        e.preventDefault();
        authenticateUser();
    }

    const authenticateUser = () =>{

        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(loginState)
        };

        fetch(`${process.env.REACT_APP_API_URL}/api/authenticate`, init)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else if (response.status === 403) {
                    return null;
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                if (data) {
                    auth.login(data.jwt_token);
                    history.push('/');
                } else {
                    setErrors(['Invalid username or password']);
                }
            })
            .catch(console.log);
    };
    

    return (
        <>
            <Header
                heading="Login to your account"
                paragraph="Don't have an account yet? "
                linkName="Register"
                linkUrl="/register"
            />
            <Errors errors={errors}/>
            <form className=" mx-auto items-center mt-8 space-y-6 w-1/2" onSubmit={handleSubmit}>
                <div className="-space-y-px">
                    {
                        fields.map(field =>
                            <Input
                                key={field.id}
                                handleChange={handleChange}
                                value={loginState[field.id]}
                                labelText={field.labelText}
                                labelFor={field.labelFor}
                                id={field.id}
                                name={field.name}
                                type={field.type}
                                isRequired={field.isRequired}
                                placeholder={field.placeholder}
                            />

                        )
                    }
                </div>

                <FormExtra />
                <FormAction handleSubmit={handleSubmit} text="Login" />


            </form>
        </>
    )
}

export default Login;