import React, { useEffect } from 'react';
import {WarehouseRack, HoverProvider}from './WarehouseRack';
import { Text } from '@react-three/drei';

const Aisle = ({ aisleId, bayCnt, position, rowCnt, rowwidth, rowLength , rowHeight = 30, tierCnt, splitByWidth,aisleLength, aisleWidth }) => {
    
  const [x, y, z] = position;

  const textPosition = splitByWidth? [x + (aisleWidth/2) , y + (rowHeight/2) + 0.5, z + 20] : [x + 20, y + (rowHeight/2) + 0.5, z + (aisleLength/2) ];

  const rackPositions = Array.from({ length: rowCnt }, (_, index) => {
    const position = splitByWidth ? [x + index * (rowwidth), y, z] : [x, y, z + index * (rowLength )];
    return position;
   });
   
   useEffect(()=>{
    // console.log(aisleLength,aisleWidth,rowwidth,rowLength,splitByWidth)
    // console.log(rackPositions);
   },[])

  return (
    <HoverProvider>
      <Text position={textPosition} 
            fontSize={25} 
            anchorX="middle" anchorY="center" 
            material-color="white"
            rotation={[ - (Math.PI / 2)  , 0, 0]} 
            style={{
                fontFamily: 'Arial, sans-serif',
                fontWeight: 'bold', // Make the text bold
                fontStyle: 'italic', // Make the text italic
                textTransform: 'uppercase', // Convert text to uppercase
                letterSpacing: '1px', // Add letter spacing
                textDecoration: 'underline', // Add underline
                textShadow: '2px 2px 4px rgba(0, 0, 0, 0.5)', // Add text shadow
                color: 'black', // Change text color
                backgroundColor: 'lightgrey', // Change background color
                padding: '5px 10px', // Add padding
                borderRadius: '5px', // Add border radius
              }}
            >
        {aisleId}
      </Text>

      {rackPositions.map((rackPosition,index) => (
        <WarehouseRack 
          key={`aisle_${aisleId}_rack${index}`}
          position={rackPosition}
          numShelves={tierCnt} 
          height={rowHeight}
          width={rowwidth}
          length={rowLength}
          bayCnt = {bayCnt}
          splitByWidth = {splitByWidth}
          rackId={`R${index + 1} - ${aisleId}`}
        />
      ))}
    </HoverProvider>
  );
};

export default Aisle;
