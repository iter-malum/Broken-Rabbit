const fs = require('fs');
const path = require('path');

const colors = [
  ['#667eea', '#764ba2'],
  ['#ff6b6b', '#ffd93d'],
  ['#4facfe', '#00f2fe'],
  ['#fa709a', '#fee140'],
  ['#a8edea', '#fed6e3'],
  ['#ff9a9e', '#fecfef'],
];

const [primary, secondary] = colors[Math.floor(Math.random() * colors.length)];
const speed = (Math.random() * 0.8 + 0.8).toFixed(2);

const envContent = `REACT_APP_RABBIT_COLOR=${primary}
REACT_APP_RABBIT_COLOR_SECONDARY=${secondary}
REACT_APP_RABBIT_SPEED=${speed}
`;

fs.writeFileSync(path.join(__dirname, '../.env'), envContent);
console.log(`🎨 Rabbit config generated: ${primary} / ${secondary} / ${speed}x`);