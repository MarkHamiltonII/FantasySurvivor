import { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { signupFields } from "../constants/formFields";
import Errors from './Errors';
import FormAction from './FormAction';
import Header from "./Header";
import Input from './Input';

const fields = signupFields;
let fieldsState = {};
fields.forEach(field => fieldsState[field.id] = '');

function Register() {
    const [registerState, setRegisterState] = useState(fieldsState);
    const [errors, setErrors] = useState([]);


    const history = useHistory();

    const handleChange = (e) => {
        setRegisterState({ ...registerState, [e.target.id]: e.target.value })
    }

    const handleSubmit=(e)=>{
        e.preventDefault();
        if (registerState.password !== registerState.confirm_password) {
            setErrors(['Passwords must match']);
            return;
        }
        createUser();
    }

    const createUser = () =>{

        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(registerState)
        };

        fetch(`${process.env.REACT_APP_API_URL}/api/create_account`, init)
            .then(response => {
                if (response.status === 201 || response.status === 400) {
                    return response.json();
                } else if (response.status === 403) {
                    return null;
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                if (data.appUserId) {
                    history.push("/confirmation", { msg: `${data.username}: account created! Log in to get started!` });
                } else {
                    setErrors(data);
                }
            })
            .catch(console.log);
    };
    

    return ( 
        <>
            <Header
                heading="Create an account"
                paragraph="Already have an account? "
                linkName="Login"
                linkUrl="/login"
            />
            <Errors errors={errors}/>
            <form className=" mx-auto items-center mt-8 space-y-6 w-1/2" onSubmit={handleSubmit}>
                <div className="-space-y-px">
                    {
                        fields.map(field =>
                            <Input
                                key={field.id}
                                handleChange={handleChange}
                                value={registerState[field.id]}
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

                <FormAction handleSubmit={handleSubmit} text="Register" />


            </form>
        </>
    )
}

export default Register;