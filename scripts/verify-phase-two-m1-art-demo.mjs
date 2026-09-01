import fs from 'node:fs'
import path from 'node:path'

const root = path.resolve(new URL('..', import.meta.url).pathname)

function read(relativePath) {
  return fs.readFileSync(path.join(root, relativePath), 'utf8')
}

function assert(condition, message) {
  if (!condition) {
    throw new Error(message)
  }
}

const componentLoader = read('frontend/src/router/core/ComponentLoader.ts')
const backendMenu = read('frontend/src/utils/backend-menu.ts')
const serviceSql = read('service.sql')

const requiredDemoPaths = [
  '/dashboard/console',
  '/dashboard/analysis',
  '/examples/tables/basic',
  '/examples/forms',
  '/widgets/icon',
  '/template/cards',
  '/result/success',
  '/safeguard/server',
  '/article/list'
]

assert(
  componentLoader.includes('../../views/**/*.vue'),
  'ComponentLoader must allow dynamic loading of Art demo pages under src/views.'
)
assert(
  backendMenu.includes('../views/**/*.vue'),
  'backend-menu component existence check must include Art demo pages under src/views.'
)
assert(
  serviceSql.includes("'Art 示例'") && serviceSql.includes("'art_demo'"),
  'service.sql must seed an active Art demo menu root.'
)

for (const demoPath of requiredDemoPaths) {
  const permission = `art${demoPath.replaceAll('/', ':')}:view`
  assert(serviceSql.includes(`'${demoPath}'`), `service.sql must seed Art demo path ${demoPath}.`)
  assert(
    serviceSql.includes(`'${permission}'`),
    `service.sql must seed permission ${permission}.`
  )
}

console.log('phase-two M1 Art demo checks passed')
