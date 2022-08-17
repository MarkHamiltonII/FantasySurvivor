import {Link} from 'react-router-dom';

export default function Header({
    heading,
    paragraph,
    linkName,
    linkUrl="#"
}){
    return(
        <div className="mt-20 mb-10">
            <div className="flex justify-center">
            </div>
            <h2 className="mt-10 text-center text-4xl font-extrabold text-gray-900">
                {heading}
            </h2>
            <p className="text-center text-sm text-gray-600 mt-6">
            {paragraph} {' '}
            <Link to={linkUrl} className="font-medium text-green-600 hover:text-green-500">
                {linkName}
            </Link>
            </p>
        </div>
    )
}