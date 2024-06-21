import React from 'react';
import { Box , Text} from '@react-three/drei';

const EntireRoom = ({ length, width, height}) => {
  length = length + (length/5)
  width = width + (width/5)

  const halfLength = length / 2;
  const halfwidth = width / 2;
  const floorColor = '#808080';
  const wallColor = '#A9A9A9';

  return (
    <>
      {/* Back Wall,  Front Wall ,Left Wall, Right Wall ,Floor */}
      <Box position={[0, 0 - 0.3, -halfwidth]} args={[length, height, 0.1]} material-color={wallColor}/>
      <Box position={[0, 0 - 0.3, halfwidth]} args={[length, height, 0.1]} material-color={wallColor}/>
      <Box position={[-halfLength , 0 -0.3, 0]} args={[0.1, height, width]} material-color={wallColor}/>
      <Box position={[halfLength, 0 - 0.3, 0]} args={[0.1, height, width]} material-color={wallColor}/>
      <Box position={[0, -height / 2 - 0.3, 0]} args={[length, 0.1, width]} material-color={floorColor} />
    
    </>

  );
};

export default EntireRoom;











