function Errors({ errors }) {
    if (!errors || errors.length === 0) {
      return null;
    }
  
    return (
      <div className=" w-1/2 mx-auto p-4 mb-5 border rounded bg-red-300 text-red-900 border-red-600">
        <p className=" font-bold">The following errors were found:</p>
        <ul>
          {errors.map(error => (
            <li key={error} className="font-semibold">{error}</li>
          ))}
        </ul>
      </div>
    );
  }
  
  export default Errors;