import React from 'react'
import { render } from 'react-native-testing-library'
import App from './App'
import { act } from 'react-test-renderer'

jest.useFakeTimers()
jest.mock('react-native/Libraries/Animated/src/NativeAnimatedHelper')

describe('<App />', () => {
  const component = render(<App />)

  it('App', async () => {
    await act(async () => {
      expect(component.toJSON()).toMatchSnapshot()
    })
  })
})
