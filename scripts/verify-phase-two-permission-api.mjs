const baseUrl = process.env.EG_SERVICE_BASE_URL || 'http://localhost:12345'
const superAdminPhone = process.env.EG_SERVICE_SUPER_ADMIN_PHONE || '18888888888'
const guestPhone = process.env.EG_SERVICE_GUEST_PHONE || '18800000000'
const password = process.env.EG_SERVICE_PASSWORD || '123456'

const artDemoPaths = [
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

const firstPhasePermissions = [
  'home:view',
  'system:user:view',
  'system:user:create',
  'system:user:update',
  'system:user:delete',
  'system:role:view',
  'system:role:create',
  'system:role:update',
  'system:role:delete',
  'system:role:permission',
  'system:menu:view',
  'system:menu:create',
  'system:menu:update',
  'system:menu:delete'
]

function artPermission(path) {
  return `art${path.replaceAll('/', ':')}:view`
}

function assert(condition, message, details) {
  if (!condition) {
    throw new Error(details ? `${message}: ${JSON.stringify(details)}` : message)
  }
}

async function request(path, options = {}) {
  const response = await fetch(`${baseUrl}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {})
    }
  })
  const body = await response.json().catch(() => ({}))
  return { response, body }
}

async function login(phone) {
  const { response, body } = await request('/account/login', {
    method: 'POST',
    body: JSON.stringify({ phone, password })
  })

  assert(response.ok, `${phone} login HTTP failed`, { status: response.status, body })
  assert(body.code === 0, `${phone} login business failed`, body)
  assert(body.data?.tokenValue, `${phone} login missing tokenValue`, body)

  return body.data.tokenValue
}

async function currentUser(token) {
  const { response, body } = await request('/account/admin', {
    headers: { Authorization: token }
  })

  assert(response.ok, 'current user HTTP failed', { status: response.status, body })
  assert(body.code === 0, 'current user business failed', body)
  assert(Array.isArray(body.data?.menus), 'current user missing menus', body)
  assert(Array.isArray(body.data?.permissions), 'current user missing permissions', body)
  assert(Array.isArray(body.data?.roles), 'current user missing roles', body)

  return body.data
}

function flattenMenus(menus = []) {
  return menus.flatMap((menu) => [menu, ...flattenMenus(menu.children || [])])
}

async function expectProtectedEndpointDenied(token) {
  const { body } = await request('/system/role/all', {
    headers: { Authorization: token }
  })

  assert(body.code !== 0, 'guest should be denied from role list endpoint', body)
}

async function expectAnonymousAdminDenied() {
  const { body } = await request('/account/admin')
  assert(body.code !== 0, 'anonymous user should be denied from /account/admin', body)
}

const superAdminToken = await login(superAdminPhone)
const superAdmin = await currentUser(superAdminToken)
const superAdminMenus = flattenMenus(superAdmin.menus)
const superAdminPaths = new Set(superAdminMenus.map((menu) => menu.path).filter(Boolean))
const superAdminPermissions = new Set(superAdmin.permissions)

assert(superAdmin.roles.includes('SuperAdmin'), 'super admin role missing', superAdmin.roles)
assert(!superAdminPermissions.has('*'), 'super admin should return concrete permissions, not *')
assert(
  superAdminMenus.some((menu) => menu.name === 'Art 示例' && menu.routeKey === 'art_demo'),
  'super admin missing Art demo root'
)

for (const permission of firstPhasePermissions) {
  assert(superAdminPermissions.has(permission), `super admin missing permission ${permission}`)
}

for (const path of artDemoPaths) {
  assert(superAdminPaths.has(path), `super admin missing Art demo menu path ${path}`)
  assert(
    superAdminPermissions.has(artPermission(path)),
    `super admin missing Art demo permission ${artPermission(path)}`
  )
}

const guestToken = await login(guestPhone)
const guest = await currentUser(guestToken)
const guestMenus = flattenMenus(guest.menus)
const guestPaths = new Set(guestMenus.map((menu) => menu.path).filter(Boolean))
const guestPermissions = new Set(guest.permissions)

assert(guest.roles.includes('guest'), 'guest role missing', guest.roles)
assert(guestPermissions.has('home:view'), 'guest should keep home:view permission')

for (const path of artDemoPaths) {
  assert(!guestPaths.has(path), `guest should not receive Art demo menu path ${path}`)
  assert(
    !guestPermissions.has(artPermission(path)),
    `guest should not receive Art demo permission ${artPermission(path)}`
  )
}

await expectProtectedEndpointDenied(guestToken)
await expectAnonymousAdminDenied()

console.log(
  JSON.stringify(
    {
      baseUrl,
      superAdminMenuCount: superAdminMenus.length,
      superAdminArtPathCount: artDemoPaths.filter((path) => superAdminPaths.has(path)).length,
      superAdminPermissionCount: superAdmin.permissions.length,
      guestMenuCount: guestMenus.length,
      guestPermissionCount: guest.permissions.length,
      protectedEndpointDeniedForGuest: true,
      anonymousAdminDenied: true
    },
    null,
    2
  )
)
