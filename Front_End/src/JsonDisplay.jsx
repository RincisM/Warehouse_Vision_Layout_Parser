import React from 'react';

const JsonDisplay = ({ data }) => {
  return (
    <div className="json-display">
      <h2>Response Data:</h2>
      <pre>{JSON.stringify(data, null, 2)}</pre>
    </div>
  );
};

export default JsonDisplay;
