function Errors({ errors }) {
    if (!errors || errors.length === 0) {
      return null;
    }
  
    return (
      <div className=" p-4 mb-5 border rounded font-bold bg-red-400 text-red-900 border-red-600">
        <p>The following errors were found:</p>
        <ul>
          {errors.map(error => (
            <li key={error}>{error}</li>
          ))}
        </ul>
      </div>
    );
  }
  
  export default Errors;