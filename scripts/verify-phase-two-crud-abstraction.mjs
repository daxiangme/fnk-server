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

const useCrudPath = path.join(root, 'frontend/src/hooks/core/useCrud.ts')
assert(fs.existsSync(useCrudPath), 'frontend/src/hooks/core/useCrud.ts must exist.')

const useCrud = read('frontend/src/hooks/core/useCrud.ts')
const hooksIndex = read('frontend/src/hooks/index.ts')
const userPage = read('frontend/src/views/system/user/index.vue')

for (const token of [
  'export interface CrudPageConfig',
  'export interface CrudRemoveOptions',
  'export function useCrudPage',
  'loadData',
  'resetQuery',
  'openCreate',
  'openEdit',
  'submit',
  'remove'
]) {
  assert(useCrud.includes(token), `useCrud.ts must include ${token}.`)
}

assert(hooksIndex.includes("export * from './core/useCrud'"), 'hooks index must export useCrud.')
assert(userPage.includes("import { useCrudPage } from '@/hooks/core/useCrud'"), 'user page must use useCrudPage.')
assert(!userPage.includes('const loading = ref(false)'), 'user page should not own duplicated loading state.')
assert(!userPage.includes('const saving = ref(false)'), 'user page should not own duplicated saving state.')

console.log('phase-two M3 CRUD abstraction checks passed')
