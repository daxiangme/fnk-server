import request from '@/utils/http'

export type SmsChannelPayload = Partial<Api.Messages.SmsChannelItem>
export type SmsTemplatePayload = Partial<Api.Messages.SmsTemplateItem>
export type MailAccountPayload = Partial<Api.Messages.MailAccountItem>
export type MailTemplatePayload = Partial<Api.Messages.MailTemplateItem>
export type NotifyTemplatePayload = Partial<Api.Messages.NotifyTemplateItem>

export function fetchGetSmsChannelList(params: Api.Messages.SmsChannelSearchParams) {
  return request.get<Api.Messages.SmsChannelList>({
    url: '/system/messages/sms/channel',
    params
  })
}

export function fetchGetEnabledSmsChannels() {
  return request.get<Api.Messages.SmsChannelItem[]>({
    url: '/system/messages/sms/channel/enabled'
  })
}

export function fetchSaveSmsChannel(data: SmsChannelPayload) {
  if (data.id) {
    return request.put<Api.Messages.SmsChannelItem>({
      url: `/system/messages/sms/channel/${data.id}`,
      data
    })
  }
  return request.post<Api.Messages.SmsChannelItem>({
    url: '/system/messages/sms/channel',
    data
  })
}

export function fetchDeleteSmsChannel(id: string) {
  return request.del<void>({
    url: `/system/messages/sms/channel/${id}`
  })
}

export function fetchGetSmsTemplateList(params: Api.Messages.SmsTemplateSearchParams) {
  return request.get<Api.Messages.SmsTemplateList>({
    url: '/system/messages/sms/template',
    params
  })
}

export function fetchSaveSmsTemplate(data: SmsTemplatePayload) {
  if (data.id) {
    return request.put<Api.Messages.SmsTemplateItem>({
      url: `/system/messages/sms/template/${data.id}`,
      data
    })
  }
  return request.post<Api.Messages.SmsTemplateItem>({
    url: '/system/messages/sms/template',
    data
  })
}

export function fetchDeleteSmsTemplate(id: string) {
  return request.del<void>({
    url: `/system/messages/sms/template/${id}`
  })
}

export function fetchSendSmsTemplate(data: Api.Messages.SmsSendParams) {
  return request.post<Api.Messages.SmsLogItem>({
    url: '/system/messages/sms/template/send',
    data
  })
}

export function fetchGetSmsLogList(params: Api.Messages.SmsLogSearchParams) {
  return request.get<Api.Messages.SmsLogList>({
    url: '/system/messages/sms/log',
    params
  })
}

export function fetchGetMailAccountList(params: Api.Messages.MailAccountSearchParams) {
  return request.get<Api.Messages.MailAccountList>({
    url: '/system/messages/mail/account',
    params
  })
}

export function fetchGetEnabledMailAccounts() {
  return request.get<Api.Messages.MailAccountItem[]>({
    url: '/system/messages/mail/account/enabled'
  })
}

export function fetchSaveMailAccount(data: MailAccountPayload) {
  if (data.id) {
    return request.put<Api.Messages.MailAccountItem>({
      url: `/system/messages/mail/account/${data.id}`,
      data
    })
  }
  return request.post<Api.Messages.MailAccountItem>({
    url: '/system/messages/mail/account',
    data
  })
}

export function fetchDeleteMailAccount(id: string) {
  return request.del<void>({
    url: `/system/messages/mail/account/${id}`
  })
}

export function fetchGetMailTemplateList(params: Api.Messages.MailTemplateSearchParams) {
  return request.get<Api.Messages.MailTemplateList>({
    url: '/system/messages/mail/template',
    params
  })
}

export function fetchSaveMailTemplate(data: MailTemplatePayload) {
  if (data.id) {
    return request.put<Api.Messages.MailTemplateItem>({
      url: `/system/messages/mail/template/${data.id}`,
      data
    })
  }
  return request.post<Api.Messages.MailTemplateItem>({
    url: '/system/messages/mail/template',
    data
  })
}

export function fetchDeleteMailTemplate(id: string) {
  return request.del<void>({
    url: `/system/messages/mail/template/${id}`
  })
}

export function fetchSendMailTemplate(data: Api.Messages.MailSendParams) {
  return request.post<Api.Messages.MailLogItem>({
    url: '/system/messages/mail/template/send',
    data
  })
}

export function fetchGetMailLogList(params: Api.Messages.MailLogSearchParams) {
  return request.get<Api.Messages.MailLogList>({
    url: '/system/messages/mail/log',
    params
  })
}

export function fetchGetNotifyTemplateList(params: Api.Messages.NotifyTemplateSearchParams) {
  return request.get<Api.Messages.NotifyTemplateList>({
    url: '/system/messages/notify/template',
    params
  })
}

export function fetchSaveNotifyTemplate(data: NotifyTemplatePayload) {
  if (data.id) {
    return request.put<Api.Messages.NotifyTemplateItem>({
      url: `/system/messages/notify/template/${data.id}`,
      data
    })
  }
  return request.post<Api.Messages.NotifyTemplateItem>({
    url: '/system/messages/notify/template',
    data
  })
}

export function fetchDeleteNotifyTemplate(id: string) {
  return request.del<void>({
    url: `/system/messages/notify/template/${id}`
  })
}

export function fetchSendNotifyTemplate(data: Api.Messages.NotifySendParams) {
  return request.post<Api.Messages.NotifyMessageItem[]>({
    url: '/system/messages/notify/template/send',
    data
  })
}

export function fetchGetNotifyMessageList(params: Api.Messages.NotifyMessageSearchParams) {
  return request.get<Api.Messages.NotifyMessageList>({
    url: '/system/messages/notify/message',
    params
  })
}
