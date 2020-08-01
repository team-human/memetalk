import React from 'react';
import { render } from 'react-native-testing-library';

import App from './App';

describe('<App />', () => {
  it("App", async () => {
    const component = render(<App />);
    expect(component.toJSON()).toMatchSnapshot();
  }); 
});

