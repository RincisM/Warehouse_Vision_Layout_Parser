import React, { useState, useContext, createContext } from 'react';
import { Box, Html, Edges } from '@react-three/drei';

// Create a context to manage the hover state
const HoverContext = createContext();

const WarehouseRack = ({ position, height, width, length, numShelves, splitByWidth, rackId }) => {
 const { hoveredRack, setHoveredRack } = useContext(HoverContext);
 const [x, y, z] = position;
 position = [x + length / 2, y, z + width / 2];

 const shelfWidth = width;
 const rackLength = length;
 const shelfThickness = 0.1;
 const brownColor = '#8B4513';
 const metalColor = '#C0C0C0';
 const sideColor = '#A0522D';
 const backColor = '#D2B48C';

 const shelfPositions = [
    { side: "left", position: splitByWidth ? [-width / 2, 0, 0] : [0, 0, -rackLength / 2], args: splitByWidth ? [shelfThickness, height, rackLength] : [shelfWidth - 0.05, height, shelfThickness], color: sideColor },
    { side: "right", position: splitByWidth ? [width / 2, 0, 0] : [0, 0, rackLength / 2], args: splitByWidth ? [shelfThickness, height, rackLength] : [shelfWidth - 0.05, height, shelfThickness], color: sideColor },
    { side: "center", position: [0, 0, 0], args: splitByWidth ? [shelfWidth - 0.05, height, shelfThickness] : [shelfThickness, height, rackLength], color: brownColor },
    { side: 'topShelf', position: [0, height / 2, 0], args: [shelfWidth, shelfThickness, rackLength], color: backColor },
    { side: 'bottomShelf', position: [0, -height / 2, 0], args: [shelfWidth, 0.2, rackLength], color: backColor }
 ];

 const startRange = -height / 2;
 const endRange = height / 2;
 const numPoints = numShelves + 1;
 const resultArray = Array.from({ length: numPoints }, (_, index) => startRange + index * ((endRange - startRange) / (numPoints - 1)));

 for (let i = 1; i < numShelves; i++) {
    shelfPositions.push({
      position: [0, resultArray[i], 0],
      args: [shelfWidth - 0.1, shelfThickness, rackLength],
      side: `Shelf ${i + 1}`,
      color: metalColor
    });
 }

 return (
    <group 
      position={position}
      onPointerOver={() => setHoveredRack(rackId)}
      onPointerOut={() => setHoveredRack(null)}
    >
      {shelfPositions.map((shelf) => (
        <Box key={shelf.side} position={shelf.position} args={shelf.args}>
          <meshStandardMaterial color={shelf.color} />
          {hoveredRack === rackId && (
            <Edges scale={1.05}>
              <meshBasicMaterial color="yellow" />
            </Edges>
          )}
        </Box>
      ))}
      {hoveredRack === rackId && (
        <Html position={[0, height / 2 + 0.5, 0]}>
          <div style={{ color: 'white', backgroundColor: 'black', padding: '2px 5px', borderRadius: '3px' }}>
            {rackId}
          </div>
        </Html>
      )}
    </group>
 );
};

const HoverProvider = ({ children }) => {
 const [hoveredRack, setHoveredRack] = useState(null);
 return (
    <HoverContext.Provider value={{ hoveredRack, setHoveredRack }}>
      {children}
    </HoverContext.Provider>
 );
};

export { WarehouseRack, HoverProvider };
